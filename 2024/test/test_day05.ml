open OUnit2

let tests =
  "Day05" >::: [
    "a" >:: (fun _ -> 
      assert_equal ~printer:string_of_int 3 3;
    );
  ]

  let _ = run_test_tt_main tests
