var PP = false;



function testLength(_value, _length, exactLength)
{
	var len = _value.length;
	if (exactLength) return (len == _length);
	else return (len >= _length);
}

function testNumber(_value)
{
	return !isNaN(_value);
}

function updateForm(control)
{
	if (control.value == "credit") showCC();
	else showPP();
}

function validateControl(control, name, _length)
{
	var str = control.value;
	var x = false;
	if (testLength(str,_length,true))
	{
		if (testNumber(str)) x = true;
		else alert(name + " is invalid!");
	}
	else alert(name + " must be " + _length + " digits!");
	return x;
}

function validateCreditCard(_value)
{
	var x = false;

		var str = _value.replace(/\s/g,'');
		if (testNumber(str))
		{
			var firstChar = str.charAt(0);
			var rl = -1;
			
			if (firstChar == "3") rl=15;
			else if (firstChar == "4" || firstChar == "5" || firstChar == "6") rl=16;
			
			if (rl == -1) alert("Invalid credit card number!");
			else
			{
				if (testLength(str,rl,true)) x = true;
				else alert("Inavlid credit card number!");
			}
		}
		else alert("Inavlid credit card number!");
	
	return x;
}

function validateDate(_value)
{
	var d1 = new Date(_value);
	var today = new Date();
	var x = false;
	if (d1 > today) x = true; // FIX
	else alert("Date must be at least one month greater than today's date");
	return x;
}

function validateEmail(_value)
{
	var x = false;
	var re = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
    if (re.test(String(_value).toLowerCase())) x = true;
	else alert("Invalid email address!");
	return x;
}

function validateForm()
{
	try
	{
		if (PP)
		{
			if (validatePassword(document.getElementById("pass").value, 7))
			{
				if (validateEmail(document.getElementById("ppmail").value))
				{
					alert("Payment Submitted");
				}
			}
		}
		else
		{
			if (validateCreditCard(document.getElementById("cnum").value))
			{
				if (validateControl(document.getElementById("cvc"), "CVV2/CVC", 3))
				{
					if (validateEmail(document.getElementById("email").value))
					{
						if (validateDate(document.getElementById("exp").value))
						{
							if (validateState())
							{
								if (validateControl(document.getElementById("zip"), "Zip Code", 5))
								{
									alert("Payment Submitted");
								}
							}
						}
					}
				}
			}
		}
		
	}
	catch(err) { alert(err);}
	
	return false;
}

function validatePassword(_value, minLength)
{	
	var x = false;
	if (testLength(_value, minLength, false)) x=true;
	else alert("Password must be at least " + minLength + " characters!");
	return x;
}

function validateState()
{
	var x = false;
	if (document.getElementById("state").value=="none") alert("Please select a state");
	else x = true;
	return x;
}


/* my functions */

function showCC()
{
	document.getElementById("paypallDiv").style.display = "none";
	document.getElementById("creditDiv").style.display = "block";
		
	document.getElementById("fn").required=true;
	document.getElementById("ln").required=true;
	document.getElementById("addr").required=true;
	document.getElementById("city").required=true;
	document.getElementById("zip").required=true;
	document.getElementById("email").required=true;
	document.getElementById("cname").required=true;
	document.getElementById("cvc").required=true;
		
	document.getElementById("ppmail").required=false;
	document.getElementById("pass").required=false;
	PP = false;
}
function showPP()
{
	document.getElementById("paypallDiv").style.display = "block";
	document.getElementById("creditDiv").style.display = "none";
		
	document.getElementById("fn").required=false;
	document.getElementById("ln").required=false;
	document.getElementById("addr").required=false;
	document.getElementById("city").required=false;
	document.getElementById("zip").required=false;
	document.getElementById("email").required=false;
	document.getElementById("cname").required=false;
	document.getElementById("cvc").required=false;
		
	document.getElementById("ppmail").required=true;
	document.getElementById("pass").required=true;
	PP = true;
}
function start()
{
	showCC();
}


