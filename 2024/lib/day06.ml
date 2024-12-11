
module Point = struct
  type t = { x: int; y: int }
  let create x y = { x = x; y = y }
  let x t = t.x
  let y t = t.y
  let move (x, y) a = create (a.x + x) (a.y + y)
  let to_string t = "P(" ^ (string_of_int t.x) ^ "," ^ (string_of_int t.y) ^ ")"
  let inside w h t = t.x < w && t.y < h && t.x >= 0 && t.y >= 0
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

  let size t = (t.width * t.height) 

  let width m = m.width

  let height m = m.height

  let guard m = m.guard

  let coordinates i t = Point.create (i mod t.width) (i / t.width) 

  let has_obstacle p m = List.exists ((=) p) m.obstacles
end

module Guard = struct
  type direction = North | East | South | West
  type t = { position: Point.t; direction: direction }
  type result = Outside | Turned of t | Moved of t

  let create p d = { position = p; direction = d }

  let position g = g.position

  let turn_direction d = match d with
    | North -> East
    | East  -> South
    | South -> West
    | West  -> North

  let next_point g = match g.direction with
    | North -> Point.move (0, -1) g.position
    | East  -> Point.move (1,  0) g.position
    | South -> Point.move (0,  1) g.position
    | West  -> Point.move (-1, 0) g.position

  let next m g = 
    let n = next_point g in
    if Point.inside (Matrix.width m) (Matrix.height m) n
    then
      if Matrix.has_obstacle n m
      then
        Turned (create g.position (turn_direction g.direction))
      else
        Moved (create n g.direction)
    else
      Outside

  let walk m g =
    let rec loop path m g =
      match next m g with
        | Turned ng -> loop path m ng
        | Moved  ng -> loop (path @ [ng]) m ng
        | Outside -> path @ [g]
    in
    loop [g] m g

end

let count_steps src =
  let m = Matrix.create src in
  let g = Guard.create (Matrix.guard m) North in
  Guard.walk m g
    |> List.map Guard.position
    |> List.sort_uniq compare
    |> List.length


