function deleteUrl(deleteUrl) {
    fetch(deleteUrl)
        .then(function (response) {
            return response.blob();
        })
        .then(function (myBlob) {
            console.log(myBlob);
        });
}