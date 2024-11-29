open OUnit2
open Aoc2024

let read_file f =
  let ic = open_in f in 
  let length = in_channel_length ic in
  let content = really_input_string ic length in
  close_in ic;
  content

let tests =
  "Day01" >::: [
    "add" >:: (fun _ -> 
      assert_equal ~printer:string_of_int 11 (Day01.add 9 2) ;
      assert_equal ~printer:Fun.id "Hello Test!\n" (read_file "../resources/test.txt")
    )
  ]

  let _ = run_test_tt_main tests
