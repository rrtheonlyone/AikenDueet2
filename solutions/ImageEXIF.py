import PIL.Image
import PIL.ExifTags
import requests

def ImageEXIF(JSONData):
    # Saves a list of the long lat data...
    out = []
    for i in JSONData:
        path = i["path"]
        out.append(GetImageEXIF(path))
    return out


def GetImageEXIF(image_url):
    r = requests.get(image_url)
    with open("newData",'wb') as f:
        f.write(r.content)
    f.close()

    img = PIL.Image.open("newData")
    exif_data = img._getexif()

    exif = {
        PIL.ExifTags.TAGS[k]: v
        for k, v in img._getexif().items()
        if k in PIL.ExifTags.TAGS
    }

    return exif

print(ImageEXIF([{"path": 'https://a4.pbase.com/o6/60/898660/1/109668616.mRQb2uhC.DSCF7380_ed1_W680.jpg'}]))
