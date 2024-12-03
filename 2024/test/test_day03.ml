open OUnit2
open Aoc2024

let tests =
  "Day03" >::: [
    "a" >:: (fun _ -> 
      assert_equal ~printer:string_of_int 161 (Day03.get_multiplication_summary "day03-sample");
      assert_equal ~printer:string_of_int 187825547 (Day03.get_multiplication_summary "day03")
    );
    "b" >:: (fun _ -> 
      assert_equal ~printer:string_of_int 48 (Day03.get_multiplication_summary_from_active_fragments "day03-b-sample");
      assert_equal ~printer:string_of_int 85508223 (Day03.get_multiplication_summary_from_active_fragments "day03")
    );
  ]

  let _ = run_test_tt_main tests
