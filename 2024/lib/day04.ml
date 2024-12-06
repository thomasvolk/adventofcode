
module Point = struct
  type t = (int * int)  
  let create x y = (x, y)
  let x t = fst t
  let y t = snd t
  let add a b = create ((x a) + (x b)) ((y a) + (y b))
  let positive t = (x t) >= 0 && (y t) >= 0
  let to_string t = "P(" ^ (string_of_int (x t)) ^ "," ^ (string_of_int (y t)) ^ ")"
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

  let map f t = List.mapi (fun i v -> f (coordinates i t) v) t.values
end

module Word = struct
  include String
  let of_points l = l |> List.map Point.to_string |> String.concat "-"
end

module WordRegister = Set.Make(Word)

let has_xmas_match = function
  | "XMAS" | "SAMX" -> true
  | _ -> false

let words start m =
  let rec walk_loop coordinates word step p dp =
    match Matrix.get p m with
      | Some v when step > 0 -> walk_loop (coordinates @ [p]) (word ^ v) (step - 1) (Point.add p dp) dp
      | _ -> (word, coordinates)
  in
  let walk = walk_loop [] "" 4 in
  [(0, -1); (1, -1); (1, 0); (1, 1); (0, 1); (-1, 1); (-1, 0); (-1, -1)]
      |> List.map (walk start)

let count_all src = 
  let m = Matrix.create src in
  Matrix.map (fun p _ -> words p m) m
   |> List.flatten
   |> List.filter (fun (w, _) -> has_xmas_match w)
   |> List.map (fun (_, c) -> Word.of_points c)
   |> WordRegister.of_list
   |> WordRegister.to_list
   |> List.length


