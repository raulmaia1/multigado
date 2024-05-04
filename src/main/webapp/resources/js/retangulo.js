	var canvas = document.getElementById('myCanvas');
	var context = canvas.getContext('2d');

	context.beginPath();
	context.rect(188, 50, 200, 100);
	context.fillStyle = 'none';
	context.fill();
	context.lineWidth = 7;
	context.strokeStyle = 'black';
	context.stroke();

