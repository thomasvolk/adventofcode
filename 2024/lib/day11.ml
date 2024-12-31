
module Stone = struct 
  type t = int

  let of_string = int_of_string

  let to_string = string_of_int

  let has_odd_digits s =
    let l = to_string s |> String.length in
    l mod 2 = 0 
 
  let split s = 
    let st = to_string s in
    let m = String.length st / 2 in
    [
      String.sub st 0 m;
      String.sub st m m;
    ]
    |> List.map of_string 

  let blink = function
    | 0 -> [1]
    | n when has_odd_digits n -> split n
    | n -> [n * 2024]
end

module Population = struct
  type t = int list

  let re_int = Re.Perl.compile_pat "(\\d+)"

  let load src = 
    Io.Resource.read_all src
      |> Re.all re_int
      |> List.map (fun g -> Re.Group.get g 1) 
      |> List.map int_of_string

  let blink p = p
    |> List.map Stone.blink
    |> List.flatten

  let rec blink_times t p =
    if t <= 0 then
      p
    else
      blink_times (t - 1) (blink p)
end

