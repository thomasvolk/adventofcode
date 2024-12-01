open OUnit2
open Aoc2024

let tests =
  "Day01" >::: [
    "add" >:: (fun _ -> 
      assert_equal ~printer:string_of_int 11 (Day01.add 9 2) ;
      assert_equal ~printer:string_of_int 14000 (String.length (Io.read_file "../resources/input_day01-a.txt"))
    )
  ]

  let _ = run_test_tt_main tests
