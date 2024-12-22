
module Point = struct
  type t = { x: int; y: int }
  let create x y = { x = x; y = y }
  let x t = t.x
  let y t = t.y
  let move (x, y) a = create (a.x + x) (a.y + y)
  let to_string t = "P(" ^ (string_of_int t.x) ^ "," ^ (string_of_int t.y) ^ ")"
  let inside (w, h) t = t.x < w && t.y < h && t.x >= 0 && t.y >= 0
end


module Matrix = struct
  type t = { 
    width: int;
    height: int;
    obstacles: Point.t list;
    guard: Point.t
  }

  let explode_string s = List.init (String.length s) (fun i -> String.get s i |> Char.escaped)

  let create src =
    let m = Io.Resource.read_lines src
      |> List.map explode_string in
    let find_all v = 
      m |> List.mapi (fun y r -> 
        r |>List.mapi (fun x v -> (x, y, v)) 
      )
      |> List.flatten
      |> List.filter (fun (_, _, c) -> c = v)
      |> List.map (fun (x, y, _) -> Point.create x y)
    in
    let w = List.length (List.nth m 0) in
    let h = List.length m in 
    let g = List.nth (find_all "^") 0 in
    {
      width = w;
      height = h;
      obstacles = find_all "#";
      guard = g
    }

  let add_obstacle o m = {
      width = m.width;
      height = m.height;
      obstacles = m.obstacles @ [o];
      guard = m.guard
    }

  let size t = (t.width * t.height) 

  let dimensions m = (m.width, m.height)

  let guard m = m.guard

  let coordinates i t = Point.create (i mod t.width) (i / t.width) 

  let has_obstacle p m = List.exists ((=) p) m.obstacles
end


module Step = struct
  type direction = North | East | South | West
  type t = { position: Point.t; direction: direction; last: t Option.t }
  type status = Turned | Moved | Start

  let create ~last p d = { position = p; direction = d; last = last }

  let turn_direction d = match d with
    | North -> East
    | East  -> South
    | South -> West
    | West  -> North

  let next_point s = match s.direction with
    | North -> Point.move (0, -1) s.position
    | East  -> Point.move (1,  0) s.position
    | South -> Point.move (0,  1) s.position
    | West  -> Point.move (-1, 0) s.position

  let status s = match s.last with
    | None -> Start
    | Some l when l.direction != s.direction -> Turned
    | _ -> Moved

  let to_list s =
    let rec pos_loop r = function
      | None -> r
      | Some s' -> pos_loop (r @ [s']) s'.last
    in
    pos_loop [] (Some s)

  let position s = s.position

  let is_inside m s = Point.inside (Matrix.dimensions m) s.position

  let is_outside m s = not (is_inside m s)

  let next m s = 
    let n = next_point s in
    if Matrix.has_obstacle n m then
      create ~last:(Some s) s.position (turn_direction s.direction)
    else
      create ~last:(Some s) n s.direction
end


module Guard = struct
  type t = { current: Step.t }

  let create p d = { current = Step.create ~last:None p d }

  let is_outside m g = Step.is_outside m g.current

  let is_start g = match g.current.last with
    | None -> true
    | _ -> false

  let is_loop _g = false

  let walk m g = 
    let rec next g' = 
      if is_outside m g' then
        g'
      else
        next { current = Step.next m g'.current }
    in
    next g

  let step g = g.current

end

let count_stucked_guards src =
  let m = Matrix.create src in
  let g = Guard.create (Matrix.guard m) North in
  let g' = Guard.walk m g in
  let gp = Guard.step g' 
      |> Step.to_list
      |> List.filter (Step.is_inside m)
      |> List.map Step.position
      |> List.sort_uniq compare
  in
  let rec find_loop c ol = match ol with
    | [] -> c
    | o :: tl ->
        let nm = Matrix.add_obstacle o m in
        let g' = Guard.walk nm g in
        if Guard.is_loop g' then
          find_loop (c + 1) tl
        else
          find_loop c tl
  in
  find_loop 0 gp

let count_steps src =
  let m = Matrix.create src in
  let g = Guard.create (Matrix.guard m) North in
  let g' = Guard.walk m g in
  Guard.step g' 
      |> Step.to_list
      |> List.filter (Step.is_inside m)
      |> List.map Step.position
      |> List.sort_uniq compare
      |> List.length
  


