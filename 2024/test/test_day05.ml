open OUnit2
open Aoc2024

let tests =
  "Day05" >::: [
    "a" >:: (fun _ -> 
      assert_equal ~printer:string_of_int 21 (List.length (Day05.RuleMap.create "day05-sample"));
    );
  ]

  let _ = run_test_tt_main tests
