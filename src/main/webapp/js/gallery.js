/**
 * Javascript datoteka koja se koristi kao front-end skripta, a koja omogućuje
 * rad ove web stranice. Ova datoteka sadrži kod za stvaranje gumbi sa tagovima
 * te nekoliko metoda koje koriste HTML tagovi u svojim atributima za dohvat
 * slika za pojedini tag, te za dohvat neke pojedine slike i njenog opisa.
 */

$.ajax({
	url : "rest/gallery_service/tags",
	data : {
		dummy : Math.random()
	},
	dataType : "json",
	success : function(data) {
		var tags = data;
		var html = "";
		for (var i = 0; i < tags.length; i++) {
			html += '<input class="btn btn-primary tag" type="button" value="' + htmlEscape(tags[i])
					+ '" onclick="tagChosen(this.value)">';
		}

		$("#buttons").html(html);
	}
});

var currentGalleryEntries;
function tagChosen(value) {
	$.ajax({
		url : "rest/gallery_service/" + value,
		data : {
			dummy : Math.random()
		},
		dataType : "json",
		success : function(data) {
			currentGalleryEntries = data;
			var html =  "";
			for(var i = 0; i < currentGalleryEntries.length; i++) {
				html += "<a onclick=\"pictureChosen('" 
					+ currentGalleryEntries[i].pictureName + "')\"><img src=\"rest/gallery_service/thumbnail/" 
					+ currentGalleryEntries[i].pictureName + "\"></a>"; 
			}
			
			$("#thumbnails").html(html);
		}
	});
}

function pictureChosen(pictureName) {
	var galleryEntry = currentGalleryEntries.find((ge)=> {
		return ge.pictureName === pictureName;
	})
	
	if(galleryEntry === undefined) {
		return;
	}
	
	console.log(galleryEntry);
	
	var html = '<img class="card-img-top" src="rest/gallery_service/picture/' 
				+ galleryEntry.pictureName +'">' + 
				'<br><h4><b>' + htmlEscape(galleryEntry.description) +'</b></h4><br>';
	
	html +='<div class="conatiner centered-container"><h4>';
	
	for(var i = 0; i < galleryEntry.tags.length; i++) {
		html += '<span class="badge badge-primary">'  + htmlEscape(galleryEntry.tags[i]) +  '</span>';
	}
	
	html +='</h4></div>';
	
	$("#big-picture").html(html);
}



