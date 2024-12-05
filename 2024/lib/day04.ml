
module Point = struct
  type t = (int * int)  
  let create x y = (x, y)
  let x t = fst t
  let y t = snd t
  let add a b = create ((x a) + (x b)) ((y a) + (y b))
end

module Matrix = struct

  type t = int list list

  let explode_string s = List.init (String.length s) (fun i -> String.get s i |> Char.escaped)

  let read src =
    Io.Resource.read_lines src
    |> List.map explode_string

  let get p m = 
    match List.nth_opt m (Point.y p) with
      | None -> None
      | Some r -> match List.nth_opt r (Point.x p) with
        | None -> None
        | v -> v

  let dimensions m = 
    let w = List.length (List.nth m 0) in
    let h = List.length m in 
    (w, h)

end

let value = function
  | "XMAS" | "SAMX" -> 1
  | _ -> 0

let words p m =
  let rec walk_loop a step p f =
    match Matrix.get p m with
      | None -> a
      | Some _ when step <= 0 -> a
      | Some v -> walk_loop (a ^ v) (step - 1) (Point.add p (f p)) f
  in
  let walk = walk_loop "" 4 in
  [(0, -1); (1, -1); (1, 0); (1, 1); (0, 1); (-1, 1); (-1, 0); (-1, -1)]
      |> List.map (fun d -> walk p (fun p -> Point.add p d))

let count _p _m = 0

let count_all src =
  let m = Matrix.read src in
  let (w, h) = Matrix.dimensions m in
  let cols v r = function
    | c when c <= 0 -> v
    | c -> v + (count (Point.create c r) m)
  in
  let rows v = function
    | r when r <= 0 -> v
    | r -> cols v r w
  in
  rows 0 h
