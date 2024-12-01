open OUnit2
open Aoc2024

let tests =
  "Day01" >::: [
    "add" >:: (fun _ -> 
      assert_equal ~printer:string_of_int 1666427 Day01.total_distance 
    )
  ]

  let _ = run_test_tt_main tests
