// NO GLOBAL VARS
// TWO DECIMAL PLACES [EXCEPT calcMode() & performStatistics()]
// USE EXCEL TO VERIFY RESULTS
// ONLT performStatistics() should interface with HTML
// DO NOT DUPLICATE CODE

function performStatistics()
{
	try
	{
		var dataStr = document.getElementById("input").value.trim();
		var data = dataStr.split(" ").map(Number);
		data.sort((a, b) => a - b);
		document.getElementById("avg").value=round(calcMean(data));
		document.getElementById("med").value=round(calcMedian(data));
		document.getElementById("mod").value=calcMode(data);
		document.getElementById("std").value=round(calcStdDev(data));
		document.getElementById("sum").value=round(calcSum(data));
		document.getElementById("var").value=round(calcVariance(data));
		document.getElementById("max").value=round(findMax(data));
		document.getElementById("min").value=round(findMin(data));
	}
	catch(err) { alert("ERROR: "+err); }
	return false;
}

function calcMean(array)
{
	var sum = calcSum(array);
	var len = array.length;
	return (sum/len);
}

function calcMedian(array)
{
	var median = "err";
	var len = array.length;
	if ((len%2)==0)
	{
		var tmp = len/2;
		median = (array[tmp]+array[tmp-1])/2.0;
	}
	else median = array[(len-1)/2];
	return median;
}

// CHECK INSTRUCTIONS
function calcMode(array)
{
	var c = array[0];
	var count = 0;
	var m = new Array();
	array.forEach(function(v)
	{
		if (v != c) 
		{
			m[m.length]=[c, count];
			c = v;
			count = 1;
		}
		else count++;
	});
	m[m.length]=[c, count];


	var same = true;
	var pp = m[0][1];
	m.forEach(function(v)
	{
		if (v[1] != pp)
		{
			same = false;
			if (v[1] > pp) pp = v[1];
		}
	});
	
	if (!same)
	{
		var ppList = new Array();
		m.forEach(function(v)
		{
			if (v[1] === pp) ppList[ppList.length]=v[0];
		});

		return ppList;
	}
	else return "None";
}

function calcStdDev(array)
{
	return Math.sqrt(calcVariance(array));
}

function calcSum(array)
{
	var sum = 0;
	array.forEach(function(element)
	{
		sum+=element;
	});
	return sum;
}

function calcVariance(array)
{
	var avg = calcMean(array);
	var squareDiffs = array.map(function(value)
	{
		var diff = value - avg;
		return diff * diff;
	});
	return calcMean(squareDiffs);
}

function findMax(array)
{
	return array[array.length-1];
}

function findMin(array)
{
	return array[0];
}

function round(value) 
{
	var decimals = 2;
	return (Number(Math.round(value+'e'+decimals)+'e-'+decimals)).toFixed(2);
}
