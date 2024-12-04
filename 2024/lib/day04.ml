let explode_string s = List.init (String.length s) (String.get s)

let read_matrix src =
  Io.Resource.read_lines src
   |> List.map explode_string

type direction = Horizontal | Vertical | DiaLeftRight | DiaRightLeft

let dimensions matrix = 
  let w = List.length (List.nth matrix 0) in
  let h = List.length matrix in 
  (w, h)

let get x y matrix = 
  match List.nth_opt matrix y with
    | None -> None
    | Some r -> match List.nth_opt r x with
      | None -> None
      | v -> v
