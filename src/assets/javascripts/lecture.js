$(function() {
  $('#note-add').click(function() {
    $(this).hide();
    $('#note-form').slideDown();
  });
  
  $('#note-cancel').click(function() {
    $('#note-add').show();
    $('#note-form').slideUp();
  });
  
  $('#note-save').click(function(e) {
    //$.post(window.location.pathname + "/note", "hallo i bims");
    $.post("/lectures/fernsehsendung/lz3/note", $("#note-form").serialize(), function(response) {
      $('#note-form').hide();
      $('#note-content').text(response).slideDown();
    });
    e.preventDefault();
  });
});