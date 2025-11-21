import ctypes
import ctypes.util
import os

    # --- Windows ---
    # Используем 'windll' для библиотек, использующих конвенцию stdcall
user32 = ctypes.windll.user32

    # Описываем прототип функции MessageBoxW
    # (HWND, LPCWSTR, LPCWSTR, UINT) -> int
user32.MessageBoxW.argtypes = [ctypes.c_void_p, ctypes.c_wchar_p,
                                   ctypes.c_wchar_p, ctypes.c_uint]
user32.MessageBoxW.restype = ctypes.c_int

print("Вызов MessageBoxW из user32.dll...")
# 0x0000040 = MB_OK | MB_ICONINFORMATION
user32.MessageBoxW(0, "Привет из Python!", "ctypes Демо (Windows)", 0x0000040)
print("Диалоговое окно закрыто.")