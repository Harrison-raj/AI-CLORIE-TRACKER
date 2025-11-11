// Select the login form
const loginForm = document.getElementById("loginForm");

loginForm.addEventListener("submit", async (e) => {
  e.preventDefault(); // prevent page reload

  const email = document.getElementById("email").value;
  const password = document.getElementById("password").value;

  // For now, simple validation
  if (!email || !password) {
    alert("Please fill in all fields!");
    return;
  }

  // Demo login (later connect to backend)
  try {
    const response = await fetch("http://localhost:8080/api/auth/login", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ email, password }),
    });

    const data = await response.json();

    if (response.ok) {
      alert("Login successful!");
      // Save token to localStorage if using JWT
      localStorage.setItem("token", data.token);
      // Redirect to main app page
      window.location.href = "/dashboard.html";
    } else {
      alert(data.message || "Invalid username or password");
    }
  } catch (error) {
    console.error("Login error:", error);
    alert("Server not reachable!");
  }
});
