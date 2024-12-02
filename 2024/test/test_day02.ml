open OUnit2
open Aoc2024

let tests =
  "Day02" >::: [
    "a" >:: (fun _ -> 
      assert_equal ~printer:string_of_int 0 1
    )
  ]

  let _ = run_test_tt_main tests
