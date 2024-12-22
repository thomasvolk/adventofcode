open OUnit2
open Aoc2024

let tests =
  "Day06" >::: [
    "a" >:: (fun _ -> 
      let m = Day06.Matrix.create "day06-sample" in
      let open Day06.Matrix in
      assert_equal ~printer:string_of_int 10 m.width;
      assert_equal ~printer:string_of_int 10 m.height;
      assert_equal ~printer:string_of_int 8 (List.length m.obstacles);
      let open Day06.Point in
      assert_equal ~printer:string_of_int 6 (m.guard.y);
      assert_equal ~printer:string_of_int 4 (m.guard.x);

      assert_equal ~printer:string_of_int 41 (Day06.count_steps "day06-sample");
      assert_equal ~printer:string_of_int 4580 (Day06.count_steps "day06");
      (*
    *)
    );
    "b" >:: (fun _ -> 
      assert_equal ~printer:string_of_int 6 (Day06.count_stucked_guards "day06-sample");
      ()
      (* inefficient: 115.67 seconds.

      assert_equal ~printer:string_of_int 1480 (Day06.count_stucked_guards "day06");

      *)
    )
  ]

  let _ = run_test_tt_main tests
