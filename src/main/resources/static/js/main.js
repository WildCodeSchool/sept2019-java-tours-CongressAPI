function deleteUrl(deleteUrl) {
    let option = {
        method: 'DELETE',
    };
    fetch(deleteUrl, option)
        .then(function (response) {
            return response.blob();
        })
        .then(function (myBlob) {
            console.log(myBlob);
        });
}