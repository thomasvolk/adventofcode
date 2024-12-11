open OUnit2

let tests =
  "Day07" >::: [
    "a" >:: (fun _ -> 
      assert_equal ~printer:string_of_int 0 0;
    );
    "b" >:: (fun _ -> 
      assert_equal ~printer:string_of_int 0 0;
    )
  ]

  let _ = run_test_tt_main tests
