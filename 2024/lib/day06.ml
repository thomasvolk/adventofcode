
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

  let compare a b = compare a b

  let create p d = { position = p; direction = d }

  let position s = s.position

  let is_inside m s = Point.inside (Matrix.dimensions m) s.position

  let is_outside m s = not (is_inside m s)
end

module Guard = struct
  type status = Turned | Moved | Start
  type t = { vec: Vector.t; stat: status }

  let create m = { vec = m; stat = Start }

  let is_outside m g = Vector.is_outside m g.vec

  let stat g = g.stat

  let turn d = let open Vector in
    match d with
    | North -> East
    | East  -> South
    | South -> West
    | West  -> North

  let next_point s = let open Vector in
    match s.direction with
    | North -> Point.move (0, -1) s.position
    | East  -> Point.move (1,  0) s.position
    | South -> Point.move (0,  1) s.position
    | West  -> Point.move (-1, 0) s.position

  let next m g = 
    let n = next_point g.vec in
    if Matrix.has_obstacle n m then
      let tm = Vector.create g.vec.position (turn g.vec.direction) in
      { vec = tm; stat = Turned }
    else
      { vec = (Vector.create n g.vec.direction); stat = Moved }

  let vec g = g.vec

  let position g = g.vec.position
end

module VectorSet = Set.Make(Vector)

module Walk = struct
  type t = Outside of VectorSet.t | Loop of VectorSet.t

  let start m g =
    let rec walk_loop path g' =
      if Guard.is_outside m g' then
        Outside path
      else
        let v = Guard.vec g' in
        let (in_loop, path') = (VectorSet.mem v path, VectorSet.add v path) in
        if in_loop then
          Loop path
        else
          walk_loop path' (Guard.next m g')
    in
    walk_loop VectorSet.empty g
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
  match Walk.start m g with
    | Outside path -> path
      |> VectorSet.to_list
      |> List.map Vector.position
      |> List.sort_uniq compare
      |> List.length
    | _ -> 0
