// Select the register form
const registerForm = document.getElementById("registerForm");

registerForm.addEventListener("submit", async (e) => {
  e.preventDefault();

  const email = document.getElementById("email").value;
  const password = document.getElementById("password").value;

  if (!email || !password) {
    alert("Please fill in all fields!");
    return;
  }

  try {
    const response = await fetch("http://localhost:8080/api/auth/register", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ email, password }),
    });

    const data = await response.json();

    if (response.ok) {
      alert("Registration successful! Please login.");
      window.location.href = "/index.html";
    } else {
      alert(data.message || "Registration failed");
    }
  } catch (error) {
    console.error("Registration error:", error);
    alert("Server not reachable!");
  }
});
