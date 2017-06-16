var noteAdd;

$(function() {
  $('#note-add').click(function() {
    noteAdd = true;
    $(this).hide();
    $('#note-form').show();
    $('#note-delete').hide();
    $('#note-panel').slideDown();
  });
  
  $('#note-cancel').click(function(e) {
    if (noteAdd) {
      $('#note-add').show();
      $('#note-panel').slideUp();
    } else {
      $('#note-edit').show();
      $('#note-form').hide();
      $('#note-content').show();
    }
    e.preventDefault();
  });
  
  $('#note-save').click(function(e) {
    $.ajax({
      url: window.location.pathname + "/note",
      type: "POST",
      data: $('#note-form textarea').val(),
      contentType: "text/plain",
      dataType: "text",
      success: function(response) {
        $('#note-form').hide();
        $('#note-content').html(response).show();
        $('#note-edit').show();
      }
    });
    e.preventDefault();
  });
  
  $('#note-edit').click(function() {
    noteAdd = false;
    $(this).hide();
    $('#note-form textarea').val($('#note-content').html());
    $('#note-content').hide();
    $('#note-delete').show();
    $('#note-form').slideDown();
  });
  
  $('#note-delete').click(function(e) {
    if(confirm("Are you sure that you want to the note? This can not be undone!")) {
      $.ajax({
        url: window.location.pathname + "/note",
        type: "DELETE",
        success: function() {
          $('#note-add').show();
          $('#note-panel').slideUp();
          $('#note-form textarea').val("");
        }
      });
    }
    e.preventDefault();
  });
  
});