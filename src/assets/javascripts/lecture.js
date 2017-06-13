$(function() {
  $('#note-add').click(function() {
    $(this).hide();
    $('#note-form').slideDown();
  });
  
  $('#note-cancel').click(function() {
    $('#note-add').show();
    $('#note-form').slideUp();
  });
});