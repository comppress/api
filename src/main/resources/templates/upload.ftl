<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>d</title>
</head>
<body>
    <div>
        <form action="/receiveJson" method="post" enctype="multipart/form-data">
            Select RssFeeds in Json Format to upload:
            <input type="file" name="fileToUpload" id="fileToUpload">
            <input type="submit" value="Upload Json" name="submit">
        </form>
    </div>
    <br>
    <div>
        <form action="/contents" method="get" enctype="multipart/form-data">
            Get all Content in Json Format:
            <input type="submit" value="Get Content" name="submit">
        </form>
        <br>
        <form action="/publishers" method="get" enctype="multipart/form-data">
            Get all Publisher in Json Format:
            <input type="submit" value="Get Publishers" name="submit">
        </form>
        <br>
        <form action="/rssFeeds" method="get" enctype="multipart/form-data">
            Get all RssFeeds in Json Format:
            <input type="submit" value="Get RssFeeds" name="submit">
        </form>
        <br>
    </div>
    <div>
        <form action="/truncateTables" method="get" enctype="multipart/form-data">
            Reset Table RssFeed, Content and Publisher
            <input type="submit" value="Get Content" name="submit">
        </form>
        <br>
    </div>

</body>
</html>