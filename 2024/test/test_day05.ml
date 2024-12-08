open OUnit2
open Aoc2024

let tests =
  "Day05" >::: [
    "a" >:: (fun _ -> 
      let setup = Day05.Setup.create "day05-sample" in
      assert_equal ~printer:string_of_int 21 (List.length (Day05.Setup.rules setup));
    );
  ]

  let _ = run_test_tt_main tests
