
module RuleMap = struct 
  include Map.Make(String)

  let re_rule = Re.Perl.compile_pat "(\\d+)\\|(\\d+)"

  let create src =
    Io.Resource.read_all src
    |> Re.all re_rule
    |> List.map (fun g -> ((Re.Group.get g 1), (Re.Group.get g 2)))
end

