library echopraxia {
  function evaluate: (string level, dict ctx) ->
    let {
      find_object: ctx[:find_object];
      person: find_object("$.person");
    }
    person[:age] == 23;
}
