window.addEventListener('load', function() {
    var backButton = document.getElementById('back');
    
    backButton.addEventListener('click', function() {
        console.log('back!!');
        history.back();
    });
});