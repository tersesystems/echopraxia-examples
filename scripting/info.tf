library echopraxia {
  function evaluate: (string level, function ctx) ->
    let {
      find_number: ctx("find_number");
    }
    find_number("$.age") == 1;
}
