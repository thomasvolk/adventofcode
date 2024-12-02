open OUnit2
open Aoc2024

let tests =
  "Day02" >::: [
    "a" >:: (fun _ -> 
      assert_equal ~printer:string_of_int 2 (List.length (Day02.get_save_reports "day02-sample"));
      assert_equal ~printer:string_of_int 326 (List.length (Day02.get_save_reports "day02"))
    );
    "b" >:: (fun _ -> 
      assert_equal ~printer:string_of_int 4 (List.length (Day02.get_save_reports_with_tolerance "day02-sample"));
      assert_equal ~printer:string_of_int 381 (List.length (Day02.get_save_reports_with_tolerance "day02"))
    )
  ]

  let _ = run_test_tt_main tests
