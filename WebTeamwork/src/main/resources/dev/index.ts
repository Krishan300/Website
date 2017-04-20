/// If we say that '$' and 'Handlebars' are both variables of type
/// any, we lose all static checking but we don't get any error
/// messages while compiling.
///
/// In general, this is a bad thing to do because it means that it
/// is on us to make sure we are using jQuery and Handlebars
/// correctly. For this tutorial, we'll let it slide...
var $: any;
var Handlebars: any;

/// This is equivalent to 'public static main()' in Java. It runs
/// once all of the files that comprise our program have loaded.
/// In this demo, all it does is initialize the navbar and simulate
/// a click on the Welcome button.
$(document).ready(function () {
	nav.init();
	nav.onWelcomeClick();
});