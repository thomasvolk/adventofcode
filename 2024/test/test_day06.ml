open OUnit2
open Aoc2024

let tests =
  "Day05" >::: [
    "a" >:: (fun _ -> 
      let m = Day06.Matrix.create "day06-sample" in
      let open Day06.Matrix in
      assert_equal ~printer:string_of_int 10 m.width;
      assert_equal ~printer:string_of_int 10 m.height;
      assert_equal ~printer:string_of_int 8 (List.length m.obstacles);
      let open Day06.Point in
      assert_equal ~printer:string_of_int 6 (m.guard.position.y);
      assert_equal ~printer:string_of_int 4 (m.guard.position.x);
    )
  ]

  let _ = run_test_tt_main tests
