import Cookies from 'js-cookie'

// cookie存的key值
const ACCESSUSER = 'k_____accessUser'
export function getAccessUser() {
  const data = localStorage.getItem(ACCESSUSER);
  try {
    return JSON.parse(data)
  }
  catch (e) {
    return data
  }
}

export function setAccessUser(accessUser) {
  return localStorage.setItem(ACCESSUSER, JSON.stringify(accessUser))
}

export function removeAccessUser() {
  return localStorage.removeItem(ACCESSUSER)
}






