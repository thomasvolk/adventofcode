
let int_pow a b = Float.pow (float_of_int a) (float_of_int b) |> int_of_float
let (^^) = int_pow

let concat a b = (string_of_int a) ^ (string_of_int b) |> int_of_string

let bits ?(base=2) s n =
  let rec b_loop bits p n =
    match p with
    | 0 -> bits
    | p ->
        let bit = base ^^ (p - 1) in 
        let rec value d = 
          let r = n - (bit * d) in
          match r with
           | r when r >= 0 -> d
           | r when r < 0 && d > 0 -> value (d - 1)
           | _ -> 0
        in
        let v = value (base - 1) in
        let result = bits @ [v] in
        let nn = n - (bit * v) in
        b_loop result (p - 1) nn
  in
  let an = Int.abs n in
  b_loop [] s an
    

module Equation = struct
  type t = { test: int; numbers: int list }

  let create t n = { test = t; numbers = n }

  let re_int = Re.Perl.compile_pat "(\\d+)"

  let parse l =
    let n = Re.all re_int l
       |> List.map (fun g -> Re.Group.get g 1) 
       |> List.map int_of_string
    in
    match n with
     | t::num -> Some { test = t; numbers = num }
     | [] -> None

  let test e = e.test

  let load src =
    Io.Resource.read_lines src
      |> List.map parse
      |> List.filter Option.is_some
      |> List.map Option.get

  let to_string e = "Equation(test=" ^
                              (string_of_int e.test) ^ 
                            " numbers=" ^ 
                              (e.numbers |> List.map string_of_int |> String.concat ",") ^ ")"

  let to_operator = function
    | 0 -> ( * )
    | 1 -> ( + )
    | _ -> concat

  let bits_map ?(base = 2) cnt = 
    let rec op_loop r = function
     | 0 -> r
     | v -> op_loop (r @ [(bits ~base:base cnt (v - 1))]) (v - 1)
    in
    op_loop [] (base ^^ cnt)

  let is_valid ?(base = 2) e =
    let rec calculate x bl nl = 
      match (bl, nl) with
        | ([], []) -> x
        | ((b :: btl), (y :: ntl)) -> calculate ((to_operator b) x y) btl ntl
        | _ -> 0
    in
    (* we have one operator less then the count of numbers *)
    let  op_cnt = (List.length e.numbers) - 1 in
    let bm = bits_map ~base:base op_cnt in
    let results = match e.numbers with
     | a :: ntl -> bm |> List.map (fun o -> calculate a o ntl)
     | [] -> []
    in
    List.exists ((=) e.test) results

end

let sum_all_valid_equations src =
  Equation.load src
    |> List.filter Equation.is_valid
    |> List.map Equation.test
    |> List.fold_left (+) 0

let sum_all_valid_equations_with_concat src =
  Equation.load src
    |> List.filter (Equation.is_valid ~base:3)
    |> List.map Equation.test
    |> List.fold_left (+) 0
