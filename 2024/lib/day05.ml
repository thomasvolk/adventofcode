

module Setup = struct 
  type t = {
    rules: (int * int) list;
    updates: int list list
  }
  let re_rule = Re.Perl.compile_pat "(\\d+)\\|(\\d+)"
  let re_update = Re.Perl.compile_pat "(\\d+)(,\\s*\\d+)+"
  let re_int = Re.Perl.compile_pat "(\\d+)"

  let rules t = t.rules

  let updates t = t.updates

  let create src =
    let input = Io.Resource.read_all src in
    let rules =
      Re.all re_rule input
      |> List.map (fun g -> ((Re.Group.get g 1), (Re.Group.get g 2)))
      |> List.map (fun r -> ((int_of_string (fst r)), (int_of_string (snd r))))
    in
    let parse_update s = Re.all re_int s
      |> List.map (fun g -> Re.Group.get g 1) 
      |> List.map int_of_string
    in
    let updates = Re.all re_update input
      |> List.map (fun g -> (parse_update (Re.Group.get g 0)))
    in
    {
      rules = rules;
      updates = updates
    }
end

