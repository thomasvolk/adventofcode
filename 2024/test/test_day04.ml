open OUnit2
open Aoc2024

let tests =
  "Day04" >::: [
    "a" >:: (fun _ -> 
      assert_equal ~printer:string_of_int 18 (Day04.count_all "day04-sample");
      assert_equal ~printer:string_of_int 0 (Day04.count_all "day04");
    );
  ]

  let _ = run_test_tt_main tests
