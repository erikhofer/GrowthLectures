$(function() {


  // Source: https://gist.github.com/mathewbyrne/1280286
  function slugify(text)
  {
    return text.toString().toLowerCase()
      .replace(/\s+/g, '-')           // Replace spaces with -
      .replace(/[^\w\-]+/g, '')       // Remove all non-word chars
      .replace(/\-\-+/g, '-')         // Replace multiple - with single -
      .replace(/^-+/, '')             // Trim - from start of text
      .replace(/-+$/, '');            // Trim - from end of text
  }

  // Set CSRF header for all ajax requests
  var csrfHeader = $("meta[name='_csrf_header']").attr("content");
  var csrfToken = $("meta[name='_csrf']").attr("content");
  $.ajaxSetup({
    beforeSend: function(xhr) {
      xhr.setRequestHeader(csrfHeader, csrfToken);
    }
  });
  
});
