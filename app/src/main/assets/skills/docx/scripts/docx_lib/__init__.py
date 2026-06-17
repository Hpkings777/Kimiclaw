"""
docx_lib - Shared library for docx editing

Modules:
    constants: XML namespace definitions
    editing: High-level API for comments and track changes
"""

from .constants import W_NS, W14_NS, W15_NS, R_NS, WP_NS, A_NS, NS, w, r

__all__ = [
    'W_NS', 'W14_NS', 'W15_NS', 'R_NS', 'WP_NS', 'A_NS', 'NS', 'w', 'r',
]
