open OUnit2
open Aoc2024

let tests =
  "Day01" >::: [
    "add" >:: (fun _ -> 
      assert_equal ~printer:string_of_int 11 (Day01.add 9 2)
    )
  ]

  let _ = run_test_tt_main tests
