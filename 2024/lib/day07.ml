
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

  let factors numbers =
    let rec f_loop fa l r =
      let nf = l |> List.fold_left (+) 0 in
      let result = fa @ [nf] in 
      match r with
        | [] -> result
        | h :: r -> f_loop result (l @ [h]) r
    in
    numbers @
    (f_loop [] [] numbers)
      |> List.filter ((!=) 0)
      |> List.sort_uniq compare

  let is_valid e = 
    let sum = e.numbers |> List.fold_left (+) 0 in
    let prd = e.numbers |> List.fold_left (fun a b -> a * b) 1 in
    if sum > e.test || prd < e.test
    then 
      false
    else
      true
end

let sum_all_valid_equations src =
  Equation.load src
    |> List.filter Equation.is_valid
    |> List.map Equation.test
    |> List.fold_left (+) 0
