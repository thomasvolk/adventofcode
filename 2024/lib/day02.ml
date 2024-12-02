open Re

type direction = Undefined | Increasing | Decreasing | Invalid

let get_direction op c = match op with
  | None -> Undefined
  | Some p -> match p - c with
    | -1 | -2 | -3 -> Decreasing
    | 1 | 2 | 3 -> Increasing
    | _ -> Invalid


let get_reports d = 
  let re_report = Perl.compile_pat "\\d+" in
  let parse_line l = 
    Re.all re_report l |> List.map (fun groups -> Re.Group.get groups 0 |> int_of_string)
  in
  Io.Resource.read_lines d
    |> List.map parse_line

let get_save_reports src =
  let is_save r = 
    let rec is_save_loop d p l =
      match l with
      | [] -> true
      | h :: tl ->
        let new_d = get_direction p h in
        match (d, new_d) with
          | (Undefined, _) -> is_save_loop new_d (Some h) tl
          | (Increasing, Increasing) -> is_save_loop new_d (Some h) tl
          | (Decreasing, Decreasing) ->is_save_loop new_d (Some h) tl
          | (_, _) -> false
    in
    is_save_loop Undefined None r
  in
  get_reports src |> List.filter is_save

