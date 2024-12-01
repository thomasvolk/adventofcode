open Core

module File = struct

  let read_lines = In_channel.read_lines

  let fold_lines path a f = In_channel.fold_lines (In_channel.create path) ~init:a ~f:f

end
