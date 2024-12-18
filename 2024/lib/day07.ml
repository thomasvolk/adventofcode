
let int_pow a b = Float.pow (float_of_int a) (float_of_int b) |> int_of_float
let (^^) = int_pow
let int_log10 a = Float.log10 (float_of_int a) |> int_of_float

(*
let concat a b = (string_of_int a) ^ (string_of_int b) |> int_of_string
*)
let concat a b = b + (a * (10 * ((int_log10 b) +1))) 

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


let zip_bits ~transform:tranform al bl =
  let rec tr_loop r = function
    | ([], _) -> r
    | (_, []) -> r
    | (a :: atl, b :: btl) -> tr_loop (r @ [(tranform a b)]) (atl, btl)
  in
  tr_loop [] (al, bl)
    

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

  let calculate bits numbers =
    let rec calculate_loop x bl nl = 
      match (bl, nl) with
        | ([], []) -> x
        | ((b :: btl), (y :: ntl)) -> calculate_loop ((to_operator b) x y) btl ntl
        | _ -> 0
    in
    match numbers with
     | a :: ntl -> calculate_loop a bits ntl
     | [] -> 0

  let possible_results ?(base = 2) e =
    (* we have one operator less then the count of numbers *)
    let op_cnt = (List.length e.numbers) - 1 in
    let bm = bits_map ~base:base op_cnt in
    bm |> List.map (fun o -> (calculate o e.numbers, o))

  let is_valid ?(base = 2) e =
    possible_results ~base:base e 
      |> List.map (fun (r, _) -> r)
      |> List.exists ((=) e.test)

  let is_valid_with_concat e =
    let results =
      possible_results ~base:2 e 
      |> List.map (fun (r, _) -> r)
    in
    if List.exists ((=) e.test) results
    then
      true
    else
      let op_cnt = (List.length e.numbers) - 1 in
      let cbm = bits_map ~base:2 op_cnt in
      let transform = zip_bits ~transform:(fun a b -> if b = 1 then 2 else a) in
      let rec revalidate o = function
        | [] -> false
        | bm :: tl -> 
            let r = calculate (transform o bm) e.numbers in
            if r = e.test then
              true
            else
              revalidate o tl
      in
      let lower_results =
        possible_results ~base:2 e 
        |> List.filter (fun (r, _) -> e.test > r)
      in
      lower_results |> List.exists (fun (_, o) -> revalidate o cbm)
end

let sum_all_valid_equations src =
  Equation.load src
    |> List.filter Equation.is_valid
    |> List.map Equation.test
    |> List.fold_left (+) 0

let sum_all_valid_equations_with_concat src =
  Equation.load src
    |> List.filter Equation.is_valid_with_concat
    |> List.map Equation.test
    |> List.fold_left (+) 0
