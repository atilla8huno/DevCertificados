function abrirJanela(pagina, largura, altura) {

	var top = (screen.height / 2) - (altura / 2);
	var left = (screen.width / 2) - (largura / 2);
	var pars = "resizable=yes,scrollbars=yes,status=no,toolbar=no,menubar=no,"
			+ "titlebar=no,location=no,directories=no,top=" + top + ",left="
			+ left + ",width=" + largura + ",height=" + altura;

	var janela = window.open(pagina, 'relatório', pars);

	janela.focus();
}

function abrirPopupRelatorio() {
	var altura = 400;
	var largura = 600;

	abrirJanela('VisualizaCertificado', largura, altura);
}

function fecharPopup() {
	Windows.close('popup');
}
