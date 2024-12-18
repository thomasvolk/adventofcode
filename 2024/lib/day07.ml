
let int_pow a b = Float.pow (float_of_int a) (float_of_int b) |> int_of_float
let (^^) = int_pow
let int_log10 a = Float.log10 (float_of_int a) |> int_of_float

(*
let concat a b = (string_of_int a) ^ (string_of_int b) |> int_of_string
*)
let concat a b = b + (a * (10 * ((int_log10 b) +1))) 
let (++) = concat


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


  let is_valid ?(concat = false) e =
    let rec is_valid_loop c = function
      | [] -> c = e.test
      | n :: tl -> let r = 
          (is_valid_loop (c * n) tl) ||
          (is_valid_loop (c + n) tl)
          in
          if concat then r || is_valid_loop (c ++ n) tl else r
    in
    match e.numbers with
      | [] -> false
      | s :: r -> is_valid_loop s r
end

let sum_all_valid_equations src =
  Equation.load src
    |> List.filter Equation.is_valid
    |> List.map Equation.test
    |> List.fold_left (+) 0

let sum_all_valid_equations_with_concat src =
  Equation.load src
    |> List.filter (Equation.is_valid ~concat:true)
    |> List.map Equation.test
    |> List.fold_left (+) 0
