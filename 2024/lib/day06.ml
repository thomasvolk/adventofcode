
module Point = struct
  type t = { x: int; y: int }
  let create x y = { x = x; y = y }
  let x t = t.x
  let y t = t.y
  let move (x, y) a = create (a.x + x) (a.y + y)
  let to_string t = "P(" ^ (string_of_int t.x) ^ "," ^ (string_of_int t.y) ^ ")"
  let inside w h t = t.x < w && t.y < h && t.x >= 0 && t.y >= 0
end

module Guard = struct
  type direction = North | East | South | West
  type t = { position: Point.t; direction: direction }

  let create p d = { position = p; direction = d }
end

module Matrix = struct
  type t = { 
    width: int;
    height: int;
    obstacles: Point.t list;
    guard: Guard.t
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
    {
      width = w;
      height = h;
      obstacles = find_all "#";
      guard = Guard.create (List.nth (find_all "^") 0) North
    }

  let size t = (t.width * t.height) 

  let coordinates i t = Point.create (i mod t.width) (i / t.width) 
end

