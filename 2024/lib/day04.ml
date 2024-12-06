
module Point = struct
  type t = { x: int; y: int }
  let create x y = { x = x; y = y }
  let x t = t.x
  let y t = t.y
  let move (x, y) a = create (a.x + x) (a.y + y)
  let to_string t = "P(" ^ (string_of_int t.x) ^ "," ^ (string_of_int t.y) ^ ")"
  let rotate w h t = create (t.x mod w) (t.y mod h)
end

module Matrix = struct

  type t = { 
    width: int;
    height: int;
    values: string list;
  }

  let explode_string s = List.init (String.length s) (fun i -> String.get s i |> Char.escaped)

  let create src =
    let m = Io.Resource.read_lines src
    |> List.map explode_string in
    let w = List.length (List.nth m 0) in
    let h = List.length m in 
    {
      width = w;
      height = h;
      values = m |> List.flatten;
    }

  let size t = (t.width * t.height) 

  let position p t = match (t.width * (Point.y p)) + (Point.x p) with 
    | i when i < 0 -> None
    | i when i >= size t -> None
    | i -> Some i

  let coordinates i t = Point.create (i mod t.width) (i / t.width) 

  let get p t = 
    match position p t with
      | None -> None
      | Some i -> List.nth_opt t.values i

  let paths start t =
    let rec walk_loop coordinates word step p dp =
      match get p t with
        | Some v when step > 0 -> walk_loop (coordinates @ [p]) (word ^ v) (step - 1) (Point.move dp p) dp
        | _ -> (word, coordinates)
    in
    let walk = walk_loop [] "" 4 in
    [(0, -1); (1, -1); (1, 0); (1, 1); (0, 1); (-1, 1); (-1, 0); (-1, -1)]
        |> List.map (walk start)

  let walk is_start t = t.values
    |> List.mapi (fun i v ->
        match is_start v with
          | true -> Some (paths (coordinates i t) t)
          | false -> None
      )
    |> List.filter Option.is_some
    |> List.map Option.get
end

let string_of_coordinates l = l |> List.map Point.to_string |> List.sort compare |> String.concat "-"

let has_xmas_match = function
  | "XMAS" | "SAMX" -> true
  | _ -> false

let is_start = function
  | "X" | "S" -> true
  | _ -> false

let count_all src = 
  Matrix.create src
   |> Matrix.walk is_start
   |> List.flatten
   |> List.filter (fun (w, _) -> has_xmas_match w)
   |> List.map (fun (_, c) -> string_of_coordinates c)
   |> List.sort_uniq compare
   |> List.length


