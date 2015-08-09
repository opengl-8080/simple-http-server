window.addEventListener('load', function() {
    var backButton = document.getElementById('back');
    
    backButton.addEventListener('click', function() {
        history.back();
    });
});