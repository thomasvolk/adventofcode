
let int_pow a b = Float.pow (float_of_int a) (float_of_int b) |> int_of_float
let (^^) = int_pow
let int_log2 n = Float.log2 (float_of_int n) |> int_of_float

let bits n =
  let rec b_loop bits p n =
    let bit = 2 ^^ p in 
    let on = n - bit >= 0 in
    let result = bits @ [on] in
    let nn = if on then n - bit else n in
    match p with
    | 0 -> result
    | p -> b_loop result (p - 1) nn
  in
  let an = Int.abs n in
  b_loop [] (int_log2 an) an
    

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



  let is_valid e =
    (* we have one operator less then the count of numbers *)
    let _op_cnt = (2 ^^ (List.length e.numbers)) in
    true

end

let sum_all_valid_equations src =
  Equation.load src
    |> List.filter Equation.is_valid
    |> List.map Equation.test
    |> List.fold_left (+) 0
