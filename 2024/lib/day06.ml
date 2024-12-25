
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


module Move = struct
  type direction = North | East | South | West
  type t = { position: Point.t; direction: direction }

  let create p d = { position = p; direction = d }

  let turn d = match d with
    | North -> East
    | East  -> South
    | South -> West
    | West  -> North

  let next_point s = match s.direction with
    | North -> Point.move (0, -1) s.position
    | East  -> Point.move (1,  0) s.position
    | South -> Point.move (0,  1) s.position
    | West  -> Point.move (-1, 0) s.position

  let position s = s.position

  let is_inside m s = Point.inside (Matrix.dimensions m) s.position

  let is_outside m s = not (is_inside m s)
end

module Guard = struct
  type t = { current: Move.t; turns: Move.t list; last: t Option.t }
  type status = Turned | Moved | Start

  let create ?(last = None) ?(turns = []) m = { current = m; turns = turns; last = last }

  let is_outside m g = Move.is_outside m g.current

  let to_list g =
    let rec pos_loop r = function
      | None -> r
      | Some g' -> pos_loop (r @ [g']) g'.last
    in
    pos_loop [] (Some g)

  let status g = match g.last with
    | None -> Start
    | Some g' when g'.current.direction != g.current.direction -> Turned
    | _ -> Moved

  let is_in_a_loop g = 
    let oc = List.find_all ((=) g.current) g.turns |> List.length in
    oc > 1

  let next m g = 
    let n = Move.next_point g.current in
    if Matrix.has_obstacle n m then
      let tm = Move.create g.current.position (Move.turn g.current.direction) in
      create ~last:(Some g) ~turns:(g.turns @ [tm]) tm
    else
      create ~last:(Some g) ~turns:g.turns (Move.create n g.current.direction)

  let current g = g.current

  let is_inside m g = Move.is_inside m g.current

  let compare_position a b = compare a.current.position b.current.position

  let position g = g.current.position
end

module Walk = struct
  type t = Open of Guard.t | Loop of Guard.t
  
  let guard w = match w with 
    | Open g -> g
    | Loop g -> g

  let start m g =
    let rec walk_loop g' =
      if Guard.is_outside m g' then
        Open g'
      else if Guard.is_in_a_loop g' then
        Loop g'
      else
        walk_loop (Guard.next m g')
    in
    walk_loop g
end

let count_stucked_guards src =
  let m = Matrix.create src in
  let g = Guard.create (Move.create (Matrix.guard m) North) in
  let path = Walk.start m g
      |> Walk.guard
      |> Guard.to_list
      |> List.filter (Guard.is_inside m)
      |> List.sort_uniq Guard.compare_position
  in
  let rec find_loop c ol = match ol with
    | [] -> c
    | o :: tl ->
        let nm = Matrix.add_obstacle (Guard.position o) m in
        match Walk.start nm g with
          | Open _ -> find_loop c tl
          | Loop _ -> find_loop (c + 1) tl
          
  in
  find_loop 0 path


let count_steps src =
  let m = Matrix.create src in
  let g = Guard.create (Move.create (Matrix.guard m) North) in
  Walk.start m g
      |> Walk.guard
      |> Guard.to_list
      |> List.filter (Guard.is_inside m)
      |> List.sort_uniq Guard.compare_position
      |> List.length
