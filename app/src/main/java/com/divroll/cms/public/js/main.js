	$("#menu-file").click(function () {

		$('#settings').addClass('uk-hidden');
		$('#file').removeClass('uk-hidden');

	});

	$("#menu-settings").click(function () {

		$('#file').addClass('uk-hidden');
		$('#settings').removeClass('uk-hidden');
	});

	$("#menu-delete").click(function () {

		$('#li-contents').addClass('uk-active');
		$('#li-delete').removeClass('uk-active');

	});

	$("#close").click(function () {

		$('#li-contents').addClass('uk-active');
		$('#li-delete').removeClass('uk-active');

	});