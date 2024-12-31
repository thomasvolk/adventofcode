
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


module Vector = struct
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
  type status = Turned | Moved | Start
  type t = { vec: Vector.t; turns: Vector.t list; last: t Option.t; stat: status }

  let create m = { vec = m; turns = []; last = None; stat = Start }

  let is_outside m g = Vector.is_outside m g.vec

  let to_list g =
    let rec pos_loop r = function
      | None -> r
      | Some g' -> pos_loop (r @ [g']) g'.last
    in
    pos_loop [] (Some g)

  let is_in_a_loop g = 
    let oc = List.find_all ((=) g.vec) g.turns |> List.length in
    oc > 1

  let next m g = 
    let n = Vector.next_point g.vec in
    if Matrix.has_obstacle n m then
      let tm = Vector.create g.vec.position (Vector.turn g.vec.direction) in
      { last = (Some g); turns = (g.turns @ [tm]); vec = tm; stat = Turned }
    else
      { last = (Some g); turns = g.turns; vec = (Vector.create n g.vec.direction); stat = Moved }

  let vec g = g.vec

  let is_inside m g = Vector.is_inside m g.vec

  let compare_position a b = compare a.vec.position b.vec.position

  let position g = g.vec.position
end

module Walk = struct
  type t = Outside of Guard.t | Loop of Guard.t
  
  let guard w = match w with 
    | Outside g -> g
    | Loop g -> g

  let start m g =
    let rec walk_loop g' =
      if Guard.is_outside m g' then
        Outside g'
      else if Guard.is_in_a_loop g' then
        Loop g'
      else
        walk_loop (Guard.next m g')
    in
    walk_loop g
end

let find_loops m root =
  let rec find_loop pl c g =
    if Guard.is_outside m g then
      c
    else
      let g' = Guard.next m g in
      match g'.stat with
        | Moved ->
          let op = Guard.position g' in
          if List.exists ((=) op) pl then
            find_loop pl c g'
          else
            let nm = Matrix.add_obstacle op m in
            let pl' = pl @ [op] in
            (match Walk.start nm g with
              | Outside _ -> find_loop pl' c g'
              | Loop _ -> find_loop pl' (c + 1 ) g')
        | _ -> find_loop pl c g'
  in
  find_loop [] 0 root

let count_stucked_guards src =
  let m = Matrix.create src in
  let g = Guard.create (Vector.create (Matrix.guard m) North) in
  find_loops m g

let count_steps src =
  let m = Matrix.create src in
  let g = Guard.create (Vector.create (Matrix.guard m) North) in
  Walk.start m g
      |> Walk.guard
      |> Guard.to_list
      |> List.filter (Guard.is_inside m)
      |> List.sort_uniq Guard.compare_position
      |> List.length
